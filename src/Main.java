import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        MotorBuscaDOM motor = new MotorBuscaDOM();
        String caminhoArquivo = "C:\\Users\\elian\\OneDrive\\Área de Trabalho\\Nova pasta\\MotorDeBuscaDOM\\src\\documento\\verbetesWikipedia.xml";

        System.out.println("Indexando arquivo...");
        //Incializa o cache
        motor.inicializarCache(caminhoArquivo);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Digite a palavra para buscar (ou 'sair' para encerrar): ");
            String input = scanner.nextLine();
            if ("sair".equalsIgnoreCase(input)){
                break;
            }

            motor.buscar(input);
        }
        scanner.close();
    }

}
