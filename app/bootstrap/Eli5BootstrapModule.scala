package bootstrap

import com.google.inject.AbstractModule

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can update modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Eli5BootstrapModule extends AbstractModule {

  /**
    * Configures the module.
    */
  override def configure() = {
    bind(classOf[InitialData]).asEagerSingleton()
  }
}
