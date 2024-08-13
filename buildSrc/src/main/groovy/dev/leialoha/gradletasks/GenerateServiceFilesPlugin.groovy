package dev.leialoha.gradletasks

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.SourceSetContainer

class GenerateServiceFilesPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('generateServiceFiles') {
            def servicesDir = "${project.buildDir}/resources/main/META-INF/services"

            doLast {
                // Create the services directory if it doesn't exist
                project.file(servicesDir).mkdirs()

                // Define a list of interfaces or abstract classes for which you want to generate service files
                def serviceClasses = project.hasProperty('serviceClasses') ?
                        project.serviceClasses : []

                serviceClasses.each { serviceClass ->
                    // Find all classes in the project that implement or extend the service class
                    def serviceImplementations = []
                    project.sourceSets.main.output.classesDirs.each { dir ->
                        project.fileTree(dir).visit { FileVisitDetails details ->
                            if (details.file.name.endsWith('.class')) {
                                def className = details.relativePath
                                        .toString()
                                        .replace('/', '.')
                                        .replace('\\', '.')
                                        .replace('.class', '')

                                try {
                                    def clazz = Class.forName(className)
                                    def superClass = Class.forName(serviceClass)

                                    if (superClass.isAssignableFrom(clazz) && clazz != superClass) {
                                        serviceImplementations << className
                                    }
                                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                                    // Ignore classes that can't be loaded
                                }
                            }
                        }
                    }

                    if (!serviceImplementations.isEmpty()) {
                        def serviceFile = project.file("${servicesDir}/${serviceClass}")
                        serviceFile.text = serviceImplementations.join('\n')
                        println "Generated service file for ${serviceClass} with implementations: ${serviceImplementations}"
                    }
                }
            }
        }

        project.afterEvaluate {
            project.tasks.processResources.dependsOn project.tasks.generateServiceFiles
        }
    }
}
