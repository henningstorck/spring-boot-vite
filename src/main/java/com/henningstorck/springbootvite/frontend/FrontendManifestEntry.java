package com.henningstorck.springbootvite.frontend;

import java.util.List;

public record FrontendManifestEntry(String file, String name, List<String> css, String src, boolean isDynamicEntry,
									boolean isEntry, List<String> imports, List<String> dynamicImports) {
}
