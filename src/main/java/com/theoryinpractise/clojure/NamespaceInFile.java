/*
 * Copyright (c) Antony Blakey 2010.
 *
 * The use and distribution terms for this software are covered by the Eclipse Public License 1.0
 * (http://opensource.org/licenses/eclipse-1.0.php) which can be found in the file epl-v10.html
 * at the root of this distribution.
 *
 * By using this software in any fashion, you are agreeing to be bound by the terms of this license.
 *
 * You must not remove this notice, or any other, from this software.
 */

package com.theoryinpractise.clojure;

import java.io.File;

public class NamespaceInFile {

    private String namespace;
    private File sourceFile;

    public NamespaceInFile(String namespace, File sourceFile) {
        this.namespace = namespace;
        this.sourceFile = sourceFile;
    }

    public String getName() {
        return namespace;
    }

    public String getFilename() {
        String base = namespace.replace('.', File.separatorChar).replace('-', '_');
        String sourceName = sourceFile.getName();
        String suffix = sourceName.substring(sourceName.lastIndexOf("."));
        return base + suffix;
    }

    public File getSourceFile() {
        return sourceFile;
    }
}
