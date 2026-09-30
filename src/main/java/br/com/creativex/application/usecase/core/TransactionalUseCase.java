// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/


package br.com.creativex.application.usecase.core;

import br.com.creativex.domain.transaction.Transaction;

public abstract class TransactionalUseCase<I, O> implements UseCase<I, O> {

    private final Transaction tx;

    protected TransactionalUseCase(Transaction tx) {
        this.tx = tx;
    }

    @Override
    public O execute(I input) {

        boolean startedHere = false;

        try {

            if (!tx.isActive()) {
                tx.begin();
                startedHere = true;
            }

            O output = doExecute(input);

            if (startedHere) {
                tx.commit();
            }

            return output;

        } catch (Exception e) {

            if (startedHere) {
                tx.rollback();
            }

            throw e;
        }
    }

    protected abstract O doExecute(I input);
}