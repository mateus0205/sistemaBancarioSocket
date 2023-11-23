public class ContaCorrente {

    private double saldo;

    public ContaCorrente(String numeroConta, double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
        }
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean transferir(ContaCorrente destino, double valor) {
        if (sacar(valor)) {
            destino.depositar(valor);
            return true;
        }
        return false;
    }
}
