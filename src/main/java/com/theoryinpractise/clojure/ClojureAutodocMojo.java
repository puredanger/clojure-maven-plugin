/*
 * Copyright (c) Mark Derricutt 2010.
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;


/**
 * @goal autodoc
 * @phase package
 * @requiresDependencyResolution test
 */
public class ClojureAutodocMojo extends AbstractClojureCompilerMojo {
    
	/**
	 * @parameter expression="${project.name}"
	 */
	private String projectName;
	
	/**
	 * @parameter expression="${project.description}"
	 */
	private String projectDescription;
	
	/**
	 * @parameter expression="${project.build.directory}"
	 */
	private String projectBuildDir;
	
    /**
     * @parameter
     */
    private Map<String, String> autodocProps;
	
    public void execute() throws MojoExecutionException {
        Map<String,String> effectiveProps = new HashMap<String,String>();
        effectiveProps.put("name", projectName);
        effectiveProps.put("description", projectDescription);
        effectiveProps.put("param-dir", "src/main/autodoc");
        effectiveProps.put("root", ".");
        effectiveProps.put("source-path", "src/main/clojure");
        effectiveProps.put("output-path", new File(projectBuildDir, "autodoc").getAbsolutePath());
        effectiveProps.put("page-title", projectName);
        
        //effectiveProps.put("web-src-dir", "");
        //effectiveProps.put("external-doc-tmpdir", "");
        //effectiveProps.put("load-classpath", "");
        //effectiveProps.put("load-jar-dirs", "");
        //effectiveProps.put("namespaces-to-document", "");
        //effectiveProps.put("trim-prefix", "");
        //effectiveProps.put("load-except-list", "");
        //effectiveProps.put("copyright", null);
        
        if(autodocProps != null) {
        	effectiveProps.putAll(autodocProps);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("(use 'autodoc.autodoc)\n");
        sb.append("(autodoc {\n");
        for(Map.Entry<String,String> entry : effectiveProps.entrySet()) {
        	String key = entry.getKey();
        	String value = entry.getValue();
        	sb.append(" :" + key + " \"" + value + "\"\n");
        }
        sb.append("})\n");
        
        try {
            File autodocClj = File.createTempFile("autodoc", ".clj");
            final PrintWriter pw = new PrintWriter(autodocClj);
            pw.print(sb.toString());
            pw.close();
            
            getLog().info("Generating docs to " + effectiveProps.get("output-path") + " with " + autodocClj.getPath());
            getLog().debug(sb.toString());

            callClojureWith(
                    getSourceDirectories(SourceDirectory.COMPILE, SourceDirectory.TEST),
                    outputDirectory, testClasspathElements, "clojure.main",
                    new String[]{autodocClj.getPath()});
            
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
