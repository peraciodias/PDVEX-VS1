/**
* Bruno Leal Engenheiro de Software
* CREATIVEX SISTEMAS -  https://lealcreativex.com/
*
*/
package br.com.creativex;

import br.com.creativex.ui.login.LoginForm;
import javax.swing.UIManager;

/**
 * Classe principal de inicialização da aplicação ERP-PDVex-vs1.
 * 
 * Responsável pelo ponto de entrada da aplicação, configuração do tema visual
 * (Look and Feel) do sistema operacional e inicialização da tela de autenticação.
 * 
 * @author Peracio Dias
 * @version 1.0
 * @since 2026-02-01
 */
public class Main {
    /**
     * Método principal da aplicação.
     * 
     * Inicializa o look and feel com as cores do sistema operacional e exibe
     * a janela de login (LoginForm) na thread de eventos da aplicação Swing.
     * 
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // 1. Opcional: Deixa o visual com as cores do Windows/Sistema Operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Inicia o sistema obrigatoriamente pela tela de Login
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
