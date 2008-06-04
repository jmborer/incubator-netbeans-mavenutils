/*
 *  Copyright 2008 Johan AndrÃ©n.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.codehaus.mojo.nbm;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.zip.ZipArchiver;

/**
 * Create a standalone application out of the composed clusters of nbm-application
 *
 * @author <a href="mailto:johan.andren@databyran.se">Johan Andren</a>
 * @author Milos Kleint
 * @goal standalone-zip
 * @requiresProject
 */
public class CreateStandaloneMojo extends AbstractMojo {

    /**
     * The branding token for the application based on NetBeans platform.
     * @parameter expression="${netbeans.branding.token}"
     * @required
     */
    protected String brandingToken;

    /**
     * additional command line arguments that the application should always
     * be run with. Will be placed in the etc/{brandingToken}.conf file
     * Eg. 
     * -J-Dnetbeans.winsys.no_toolbars=true -J-Xdebug -J-Xnoagent -J-Xrunjdwp:transport=dt_socket,suspend=n,server=n,address=8888
     * @parameter expression="${netbeans.default.options}"
     */
    private String defaultOptions;

    /**
     * output directory where the the netbeans application will be created.
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    private File buildDirectory;
    
    /**
     * Name of the jar packaged by the jar:jar plugin
     * @parameter expression="${project.build.finalName}"
     */
    private File finalName;
    

    /**
     * The Maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * 
     * @throws org.apache.maven.plugin.MojoExecutionException 
     * @throws org.apache.maven.plugin.MojoFailureException 
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            File nbmBuildDirFile = new File(buildDirectory, brandingToken);
            
            ZipArchiver archiver = new ZipArchiver();
            archiver.addDirectory(nbmBuildDirFile);
            File zipFile = new File( buildDirectory, finalName + ".zip");
            //TODO - somehow check for last modified content to see if we shall be
            //recreating the zip file.
            archiver.setDestFile(zipFile);
            archiver.createArchive();
            project.getArtifact().setFile( zipFile );

        } catch (Exception ex) {
            throw new MojoExecutionException("", ex);
        }

    }
}