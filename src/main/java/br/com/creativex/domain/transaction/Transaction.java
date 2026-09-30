// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.domain.transaction;



public interface Transaction {
    void begin();
    void commit();
    void rollback();
    boolean isActive();

    <T> T execute(TransactionalOperation<T> operation);
}
