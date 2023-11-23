import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class ClienteBanco {

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 12345);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream())
        ) {
            realizarOperacoes(output, input);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void realizarOperacoes(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                exibirMenu();
                int opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        realizarOperacao(output, input, TipoOperacao.SAQUE);
                        break;
                    case 2:
                        realizarOperacao(output, input, TipoOperacao.DEPOSITO);
                        break;
                    case 3:
                        realizarOperacao(output, input, TipoOperacao.SALDO);
                        break;
                    case 4:
                        realizarOperacaoTransferencia(output, input);
                        break;
                    case 0:
                        System.out.println("Saindo do programa");
                        return;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            }
        }
    }

    private static void realizarOperacao(ObjectOutputStream output, ObjectInputStream input, TipoOperacao saque) {
    }

    private static void exibirMenu() {
        System.out.println("Escolha uma operação:");
        System.out.println("1. Saque");
        System.out.println("2. Depósito");
        System.out.println("3. Saldo");
        System.out.println("4. Transferência");
        System.out.println("0. Sair");
    }
    private static boolean realizarOperacaoTransferencia(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Digite o número da conta de origem: ");
            String numeroContaOrigem = scanner.next();

            System.out.print("Digite o número da conta de destino: ");
            String numeroContaDestino = scanner.next();

            System.out.print("Digite o valor da transferência: ");
            double valor = scanner.nextDouble();

            MensagemCliente mensagem = new MensagemCliente(numeroContaOrigem, TipoOperacao.TRANSFERENCIA, valor);
            mensagem.setDestino(numeroContaDestino);
            output.writeObject(mensagem);

            MensagemServidor resposta = (MensagemServidor) input.readObject();
            System.out.println(resposta.getMensagem());

            // Retorna false para indicar que o programa deve continuar
            return false;
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Certifique-se de inserir valores numéricos corretos.");
            scanner.next(); // Consumir entrada inválida
            return true;
        } finally {
            scanner.close();  // Feche o Scanner após a leitura
        }
    }
}