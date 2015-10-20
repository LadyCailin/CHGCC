package com.chllvm.core;

import com.laytonsmith.PureUtilities.CommandExecutor;
import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class CHGCC extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion("1.0.0");
	}

	@Override
	public void onStartup() {
		super.onStartup();
	}
}
