public class CalculadoraModel {

    // 1. DADOS: Campos que armazenam o estado da calculadora
    private double primeiroNumero = 0;
    private String operacao = ""; // Armazena a operação pendente (+, -, *, ÷)
    private boolean aguardandoNovoNumero = true; // Flag para saber se a próxima digitação limpa o display

    // Método que realiza a operação de fato (a lógica matemática)
    private double calcular(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "÷":
                // Proteção contra divisão por zero
                if (num2 == 0) return 0;
                return num1 / num2;
            default:
                return num2; // Se a operação for inválida
        }
    }

    // 2. LÓGICA DE NEGÓCIOS: Processa o clique em um operador ou '='
    public String processarOperacao(String valorDisplay, String novaOperacao) {

        // Converte o texto do display para um número (substitui ',' por '.' para Java)
        double numeroAtual = Double.parseDouble(valorDisplay.replace(',', '.'));

        if (operacao.isEmpty()) {
            // Se for o início do cálculo, apenas armazena o número
            primeiroNumero = numeroAtual;
        } else {
            // Se já houver uma operação pendente, resolve a expressão atual
            primeiroNumero = calcular(primeiroNumero, numeroAtual, operacao);
        }

        // Atualiza a operação pendente para a próxima etapa
        operacao = novaOperacao;
        aguardandoNovoNumero = true;

        // Formata o resultado para o display (substitui '.' por ',')
        String resultado = String.valueOf(primeiroNumero).replace('.', ',');

        // Limpa o ".0" para números inteiros (ex: 5,0 -> 5)
        if (resultado.endsWith(",0")) {
            resultado = resultado.substring(0, resultado.length() - 2);
        }

        return resultado;
    }

    // 3. MÉTODOS DE ESTADO (Getters/Setters)

    // Método chamado por 'AC' para zerar tudo
    public void reset() {
        primeiroNumero = 0;
        operacao = "";
        aguardandoNovoNumero = true;
    }

    public boolean isAguardandoNovoNumero() {
        return aguardandoNovoNumero;
    }

    public void setAguardandoNovoNumero(boolean status) {
        aguardandoNovoNumero = status;
    }

    public String getOperacao() {
        return operacao;
    }
}

