package info.archinnov.demo.embedded;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.embedded.CQLEmbeddedServerBuilder;
import info.archinnov.achilles.entity.manager.CQLPersistenceManager;

public class EmbeddedPersistenceManagerFactoryBean extends AbstractFactoryBean<CQLPersistenceManager> {
    private static CQLPersistenceManager manager;

    static {
        manager = CQLEmbeddedServerBuilder
                .noEntityPackages()
                .withKeyspaceName("demo")
                .cleanDataFilesAtStartup(true)
                .buildPersistenceManager();

        final Session session = manager.getNativeSession();

        createTables(session);
    }

    private static void createTables(Session session) {
        session.execute("CREATE TABLE countdown(id text PRIMARY KEY,value text)");
        session.execute("CREATE TABLE ratelimit(id text,column text,value text, PRIMARY KEY(id,column))");
        session.execute("CREATE TABLE time_stamp(id text PRIMARY KEY,value text)");
        session.execute("CREATE TABLE writebarrier(id text PRIMARY KEY,value text)");
    }

    @Override
    public Class<?> getObjectType() {
        return CQLPersistenceManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    protected CQLPersistenceManager createInstance() throws Exception {
        return manager;
    }
}
