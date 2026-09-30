// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

package br.com.creativex.application.usecase.core;
public interface UseCase<I, O> {
    O execute(I input);
}