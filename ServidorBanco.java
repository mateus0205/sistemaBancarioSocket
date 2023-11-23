import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorBanco {

    private Map<String, ContaCorrente> contas;

    public ServidorBanco() {
        contas = new HashMap<>();
        contas.put("123456", new ContaCorrente("123456", 1000.0));
        contas.put("789012", new ContaCorrente("789012", 500.0));
    }

    public static void main(String[] args) {
        ServidorBanco servidor = new ServidorBanco();
        servidor.iniciarServidor();
    }

    public void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clienteSocket);

                // Criando uma thread para lidar com o cliente
                Thread thread = new Thread(() -> handleCliente(clienteSocket));
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCliente(Socket clienteSocket) {
        try (
            ObjectOutputStream output = new ObjectOutputStream(clienteSocket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(clienteSocket.getInputStream())
        ) {
            while (true) {
                MensagemCliente mensagem = (MensagemCliente) input.readObject();

                if (mensagem == null) {
                    break;
                }

                MensagemServidor resposta = processarMensagem(mensagem);
                output.writeObject(resposta);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private MensagemServidor processarMensagem(MensagemCliente mensagem) {
        String numeroConta = mensagem.getNumeroConta();
        ContaCorrente conta = contas.get(numeroConta);

        if (conta == null) {
            return new MensagemServidor("Conta não encontrada");
        }

        switch (mensagem.getTipoOperacao()) {
            case SAQUE:
                double valorSaque = mensagem.getValor();
                if (conta.sacar(valorSaque)) {
                    return new MensagemServidor("Saque realizado com sucesso. Novo saldo: " + conta.getSaldo());
                } else {
                    return new MensagemServidor("Saldo insuficiente para o saque");
                }
            case DEPOSITO:
                double valorDeposito = mensagem.getValor();
                conta.depositar(valorDeposito);
                return new MensagemServidor("Depósito realizado com sucesso. Novo saldo: " + conta.getSaldo());
            case SALDO:
                return new MensagemServidor("Saldo atual: " + conta.getSaldo());
            case TRANSFERENCIA:
                String destino = mensagem.getDestino();
                double valorTransferencia = mensagem.getValor();
                ContaCorrente contaDestino = contas.get(destino);

                if (contaDestino == null) {
                    return new MensagemServidor("Conta de destino não encontrada");
                }

                if (conta.transferir(contaDestino, valorTransferencia)) {
                    return new MensagemServidor("Transferência realizada com sucesso. Novo saldo: " + conta.getSaldo());
                } else {
                    return new MensagemServidor("Saldo insuficiente para a transferência");
                }
            default:
                return new MensagemServidor("Operação inválida");
        }
    }
}
