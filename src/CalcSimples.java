import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Implementação da classe principal com ActionListener
public class CalcSimples extends JFrame implements ActionListener {

    // Instância do Model
    private final CalculadoraModel model = new CalculadoraModel(); // Supondo que CalculadoraModel existe

    // Variável de instância (campo da classe) para o display
    private JTextField display;

    // CORES FORA DO LOOP para melhor organização
    private final Color COR_OPERACAO = new Color(216, 154, 158);
    private final Color COR_NUMERO = new Color(77, 80, 97);
    private final Color COR_LIMPAR = new Color(250, 223, 99);

    public CalcSimples() {
        super("Calculadora Simples");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CalcSimples().inicializarInterface();
        });
    }

    public void inicializarInterface() {

        // Criação do Display (NORTH)
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 44));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(0, 100));
        display.setBorder(new EmptyBorder(5, 5, 5, 5));
        display.setBackground(Color.decode("#30323d"));
        display.setForeground(Color.WHITE);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Criação do Painel dos Botões (CENTER)
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(5, 4, 5, 5));
        painelBotoes.setBorder(new EmptyBorder(7, 7, 7, 7));
        painelBotoes.setBackground(Color.decode("#30323d"));

        String[] rotulosBotoes = {
                "AC", "()", "%", "÷",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ",", "C", "="
        };

        for (String rotulo : rotulosBotoes) {
            JButton botao = new JButton(rotulo);
            botao.setFont(new Font("Arial", Font.BOLD, 22));
            configurarCorBotao(botao, rotulo);
            botao.setOpaque(true);
            botao.setBorderPainted(false);
            botao.addActionListener(this);
            painelBotoes.add(botao);
        }

        add(painelBotoes, BorderLayout.CENTER);
        setSize(340, 545);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configurarCorBotao(JButton botao, String rotulo) {
        // ... (Seu método de configuração de cores permanece o mesmo) ...
        if (rotulo.equals("C") || rotulo.equals("AC")) {
            botao.setBackground(COR_LIMPAR);
            botao.setForeground(Color.white);

        } else if (rotulo.equals("÷") || rotulo.equals("*") ||
                rotulo.equals("-") || rotulo.equals("+") ||
                rotulo.equals("=") || rotulo.equals("()") ||
                rotulo.equals("%")) {
            botao.setBackground(COR_OPERACAO);
            botao.setForeground(Color.WHITE);

        } else {
            botao.setBackground(COR_NUMERO);
            botao.setForeground(Color.WHITE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        // 1. LÓGICA CORRIGIDA DOS BOTÕES NUMÉRICOS
        if ("0123456789,".contains(comando)) {

            // Se estiver AGUARDANDO novo número (após '+', '=' ou 'AC'), SUBSTITUI!
            if (model.isAguardandoNovoNumero()) {

                // Se o display já mostra um resultado de cálculo e o usuário
                // digita um novo número, o display deve ser SUBSTITUÍDO.

                if (comando.equals(",")) {
                    display.setText("0,");
                } else {
                    display.setText(comando);
                }

                // Indica ao Model que a digitação do novo número começou.
                model.setAguardandoNovoNumero(false);

                // Se NÃO estiver aguardando, ANEXA o número ao atual (ou lida com a vírgula).
            } else {
                if (comando.equals(",")) {
                    if (!display.getText().contains(",")) {
                        display.setText(display.getText() + comando);
                    }
                } else if (display.getText().equals("0")) {
                    // Troca o '0' inicial por um número ou '0,'
                    display.setText(comando.equals(",") ? "0," : comando);
                } else {
                    display.setText(display.getText() + comando);
                }
            }

            // 2. LÓGICA DE LIMPEZA
        } else if (comando.equals("AC")) {
            model.reset();
            display.setText("0");

        } else if (comando.equals("C")) {
            String textoAtual = display.getText();
            if (textoAtual.length() > 1) {
                display.setText(textoAtual.substring(0, textoAtual.length() - 1));
            } else {
                display.setText("0");
            }
            // Não resetamos o Model, mas não aguardamos mais um novo número, pois a digitação continua.
            model.setAguardandoNovoNumero(false);

            // 3. LÓGICA DE OPERAÇÕES E IGUAL
        } else if (comando.equals("=") ||
                comando.equals("+") ||
                comando.equals("-") ||
                comando.equals("*") ||
                comando.equals("÷")) {

            String novaOperacao = comando.equals("=") ? "" : comando;

            if (comando.equals("=") && model.getOperacao().isEmpty()) {
                return;
            }

            // 1. O Model calcula usando o número no display e o estado interno.
            // 2. O resultado é exibido.
            // 3. O Model seta aguardandoNovoNumero=true, para que o próximo clique numérico substitua o resultado.

            String resultado = model.processarOperacao(display.getText(), novaOperacao);
            display.setText(resultado);

            if (comando.equals("=")) {
                model.reset();
                model.setAguardandoNovoNumero(true);
            }
        }
    }
}