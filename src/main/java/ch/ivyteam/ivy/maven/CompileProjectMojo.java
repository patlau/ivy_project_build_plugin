/*
 * Copyright (C) 2016 AXON IVY AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.ivyteam.ivy.maven;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import ch.ivyteam.ivy.maven.engine.MavenProjectBuilderProxy;
import ch.ivyteam.ivy.maven.util.ClasspathJar;
import ch.ivyteam.ivy.maven.util.SharedFile;

/**
 * Compiles an ivy Project with an ivyEngine.
 * 
 * @author Reguel Wermelinger
 * @since 6.0.0
 */
@Mojo(name=CompileProjectMojo.GOAL, requiresDependencyResolution=ResolutionScope.COMPILE)
public class CompileProjectMojo extends AbstractProjectCompileMojo
{
  public static final String GOAL = "compileProject";
  
  /** 
   * Set to <code>true</code> to bypass the generation of <b>ivy data classes</b>+<b>webservice processes</b> and compilation of <b>java sources</b>.
   * @since 6.1.0
   */
  @Parameter(defaultValue="false", property="ivy.compiler.skip")
  boolean skipCompilation;

  @Override
  protected void compile(MavenProjectBuilderProxy projectBuilder) throws Exception
  {
    if (skipCompilation)
    {
      return;
    }
    
    getLog().info("Compiling ivy Project...");
    //List<File> iarJars = projectBuilder.createIarJars(getDependencies("iar"));
    //projectBuilder.compile(project.getBasedir(), iarJars, getOptions());
    //writeDependencyIarJar(iarJars);
    
    try {
    	getLog().info("Resolving localhost...");
    	InetAddress.getLocalHost();
    } catch (UnknownHostException ex) {
    	getLog().info(ex.getLocalizedMessage());
    }
    try {
    	getLog().info("Resolving localhost address...");
    	String s = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException ex) {
    	getLog().info(ex.getLocalizedMessage());
    }
    try {
    	getLog().info("Resolving localhost name...");
    	String s = InetAddress.getLocalHost().getCanonicalHostName();
    } catch (UnknownHostException ex) {
    	getLog().info(ex.getLocalizedMessage());
    }
    
    
    
    List<File> iarJars = resolveIarDependencies();
    projectBuilder.compile(project.getBasedir(), iarJars, getLog());
    writeDependencyIarJar(iarJars);
    
  }
  
  private List<File> resolveIarDependencies()
  {
	getLog().info("Resolve dependencies...");
    Set<org.apache.maven.artifact.Artifact> dependencies = project.getArtifacts();
    if (dependencies == null)
    {
      return Collections.emptyList();
    }
    
    List<File> dependentIars = new ArrayList<>();
    for(org.apache.maven.artifact.Artifact artifact : dependencies)
    {
      if (artifact.getType().equals("iar"))
      {
        dependentIars.add(artifact.getFile());
      }
    }
    return dependentIars;
  }
  
  private void writeDependencyIarJar(Collection<File> iarJarDepenencies) throws IOException
  {
		getLog().info("Write dependency iar...");
    if (iarJarDepenencies == null)
    { // no dependencies
      return;
    }
    File jar = new SharedFile(project).getIarDependencyClasspathJar();
    new ClasspathJar(jar).createFileEntries(iarJarDepenencies);
  }

}
