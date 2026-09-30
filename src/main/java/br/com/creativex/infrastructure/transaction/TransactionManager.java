// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
// creativex sistemas
package br.com.creativex.infrastructure.transaction;


import br.com.creativex.domain.transaction.Transaction;
import br.com.creativex.domain.transaction.TransactionalOperation;
import java.sql.Connection;

public class TransactionManager implements Transaction {



    @Override
    public <T> T execute(TransactionalOperation<T> operation) {
        try {
            begin();
            T result = operation.execute(connection);
            commit();
            return result;
        } catch (Exception e) {
            rollback();
            throw new RuntimeException("Erro na transação: " + e.getMessage(), e);
        }
    }

    private final Connection connection;

    private final ThreadLocal<Boolean> active =
            ThreadLocal.withInitial(() -> false);

    public TransactionManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void begin() {

        try {

            if (!active.get()) {
                connection.setAutoCommit(false);
                active.set(true);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao iniciar transação", e);
        }
    }

    @Override
    public void commit() {

        try {

            if (active.get()) {
                connection.commit();
                connection.setAutoCommit(true);
                active.set(false);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao confirmar transação", e);
        }
    }

    @Override
    public void rollback() {

        try {

            if (active.get()) {
                connection.rollback();
                connection.setAutoCommit(true);
                active.set(false);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao desfazer transação", e);
        }
    }

    @Override
    public boolean isActive() {
        return active.get();
    }
}