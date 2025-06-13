package com.henningstorck.springbootvite.frontend;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

@Service
public class FrontendService {
	public static final String DEV_SERVER_HOST = "localhost";
	public static final int DEV_SERVER_PORT = 5173;
	public static final List<String> DEV_SERVER_ASSETS = List.of("http://localhost:5173/@vite/client", "http://localhost:5173/src/main.ts");

	public List<String> getStylesheets() {
		return getAssets().stream().filter(asset -> asset.endsWith(".css")).toList();
	}

	public List<String> getScripts() {
		return getAssets().stream().filter(asset -> !asset.endsWith(".css")).toList();
	}

	private List<String> getAssets() {
		if (isDevServerRunning()) {
			return DEV_SERVER_ASSETS;
		}

		return Collections.emptyList();
	}

	private boolean isDevServerRunning() {
		try (Socket socket = new Socket(DEV_SERVER_HOST, DEV_SERVER_PORT)) {
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
