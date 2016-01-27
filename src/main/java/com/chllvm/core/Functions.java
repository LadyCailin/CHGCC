package com.chllvm.core;

import com.laytonsmith.PureUtilities.CommandExecutor;
import com.laytonsmith.PureUtilities.Common.FileUtil;
import com.laytonsmith.PureUtilities.Common.OSUtils;
import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREIOException;
import com.laytonsmith.core.exceptions.CRE.CREPluginInternalException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class Functions {
	@api
	public static class cpp_eval extends AbstractFunction {

		public Class[] thrown() {
			return new Class[]{CREIOException.class, CREPluginInternalException.class};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return null;
		}

		File tempDir = new File(".temp");
		public Construct exec(Target t, Environment environment, Construct... args) throws ConfigRuntimeException {
			try {
				// Test for GCC
				CommandExecutor.Execute("g++ --version");
			} catch (InterruptedException ex) {
				Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				throw new CREIOException("g++ must first be installed on your system", t, ex);
			}
			try {
				String script = args[0].val();
				tempDir.mkdirs();
				File scriptLocation = new File(tempDir, "temp.cpp");
				String ext = "";
				if(OSUtils.GetOS() == OSUtils.OS.WINDOWS){
					ext = ".exe";
				}
				File execLocation = new File(tempDir, "temp" + ext);
				FileUtil.write(script, scriptLocation);
				CommandExecutor ce = new CommandExecutor(
					new String[]{"g++", scriptLocation.getCanonicalPath(), "-o", execLocation.getCanonicalPath()});
				final StringBuffer error = new StringBuffer();
				ce.setSystemErr(new BufferedOutputStream(new OutputStream() {

					@Override
					public void write(int b) throws IOException {
						error.append((char)b);
					}
				}));
				int status = ce.start().waitFor();
				if(status == 0){
					CommandExecutor exec = new CommandExecutor(new String[]{execLocation.getCanonicalPath()});
					exec.setSystemInputsAndOutputs();
					exec.start().waitFor();
				} else {
					throw new CREPluginInternalException(error.toString(), t);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				throw new CREIOException(ex.getMessage(), t, ex);
			} finally {
				FileUtil.recursiveDelete(tempDir);
			}
			return CVoid.VOID;
		}

		public String getName() {
			return "cpp_eval";
		}

		public Integer[] numArgs() {
			return new Integer[]{Integer.MAX_VALUE};
		}

		public String docs() {
			return "void {script, [gccOptions], [args...]} Compiles and executes arbitrary C/C++. The args are sent"
				+ " as commandline arguments to the executable. The main function is not automatically"
				+ " added for you, so you must provide a full main function. GCC must already be installed on your system.";
		}

		public Version since() {
			return new SimpleVersion("1.0.0");
		}

	}
}
