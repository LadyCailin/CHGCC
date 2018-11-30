package com.chllvm.core;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;

/**
 * 
 */
public class CHGCC extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion(1, 0, 3, "SNAPSHOT");
	}

	@Override
	public void onStartup() {
		super.onStartup();
	}
}
