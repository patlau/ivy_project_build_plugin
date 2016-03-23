/*
 * Copyright (C) 2016 AXON IVY AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ch.ivyteam.ivy.maven.engine;

import java.io.File;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import ch.ivyteam.ivy.maven.util.SharedFile;

public class EngineMojoContext
{
  public final File engineDirectory;
  public final MavenProject project;
  public final Log log;
  public final EngineVmOptions vmOptions;
  public final String engineClasspathJar;
  public final MavenProperties properties;
  public final File engineLogFile;

  public EngineMojoContext(File engineDirectory, MavenProject project, Log log, File engineLogFile, EngineVmOptions vmOptions)
  {
    this.engineDirectory = engineDirectory;
    this.project = project;
    this.log = log;
    this.engineLogFile = engineLogFile;
    this.vmOptions = vmOptions;

    this.engineClasspathJar = new SharedFile(project).getEngineClasspathJar().getAbsolutePath();
    this.properties = new MavenProperties(project, log);

    if (!(new File(engineClasspathJar).exists()))
    {
      throw new RuntimeException("Engine ClasspathJar " + engineClasspathJar + " does not exist.");
    }
    if (!(engineDirectory.exists()))
    {
      throw new RuntimeException("Engine Directory " + engineDirectory + " does not exist.");
    }
  }
}