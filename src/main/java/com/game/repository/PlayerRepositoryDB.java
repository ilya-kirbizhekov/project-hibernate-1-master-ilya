package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;


    public PlayerRepositoryDB() {

        Properties properties = new Properties();
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/rpg");
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "p@ssw0rd");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .addProperties(properties)
                .buildSessionFactory();

    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {
           NativeQuery<Player> query = session.createNativeQuery("SELECT * FROM rpg.player", Player.class);
           query.setFirstResult(pageNumber*pageSize);
           query.setMaxResults(pageSize);
           return query.list();


        }

    }

    @Override
    public int getAllCount() {
        try(Session session = sessionFactory.openSession()) {
           Query<Long> query = session.createNamedQuery("player_getAllCount", Long.class);
           return Math.toIntExact(query.uniqueResult());
        }
    }

    @Override
    public Player save(Player player) {

        try(Session session = sessionFactory.openSession()) {
           session.save(player);
           return player;
        }

    }

    @Override
    public Player update(Player player) {
        return null;
    }

    @Override
    public Optional<Player> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Player player) {

    }

    @PreDestroy
    public void beforeStop() {

        sessionFactory.close();
    }
}