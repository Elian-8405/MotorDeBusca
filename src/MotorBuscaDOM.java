import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class MotorBuscaDOM {

    // Estrutura de cache: palavra -> lista de informações
    private final Map<String, List<ElementoInfo>> cache = new HashMap<>();
    private boolean cacheInicializado = false;

    // Inicializa o cache lendo e processando o arquivo XML
    public void inicializarCache(String caminhoArquivo) {
        //Verifica se o cache foi inicializado
        if (cacheInicializado) return;

        try {
            /**
             * Bloco com o processamento/normalizacao e obtencao de informacao
             * do XML
             *
             *
             * */
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(caminhoArquivo));


            document.getDocumentElement().normalize();

            //Pega todos os nodos das tags <page>s
            NodeList pages = document.getElementsByTagName("page");

            for (int i = 0; i < pages.getLength(); i++) {
                Node pageNode = pages.item(i);
                //Verifica se o nodo é um elemento
                if (pageNode.getNodeType() == Node.ELEMENT_NODE) {
                    //Faz um casting do nodo se for um elemento
                    Element pageElement = (Element) pageNode;

                    //Pega as informações com o metodo getTagValue
                    String id = getTagValue("id", pageElement);
                    String title = getTagValue("title", pageElement);
                    String text = getTagValue("text", pageElement);

                    //Inicializa o cache
                    indexarPagina(id, title, text);
                }
            }

            cacheInicializado = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Extrai o valor de uma tag específica dentro de um elemento
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);

        if(nodeList.getLength() > 0){
            return nodeList.item(0).getTextContent();

        }else {
            return "";
        }
    }

    // Faz o processamento do cache
    private void indexarPagina(String id, String titulo, String texto) {
       //Splita o texto com espaço em branco
        String[] palavras = texto.split("\\s+");
        //Itera sobre as palavras do texto
        for (String palavra : palavras) {
            //Nomaliza
            String palavraNormalizada = palavra.toLowerCase().replaceAll("[^a-z0-9]", "");
            //Remove as stopWords
            if (palavraNormalizada.length() > 4) {

                cache.computeIfAbsent(palavraNormalizada, k -> new ArrayList<>());

                // Verifica se o elemento já existe na lista
                List<ElementoInfo> elementos = cache.get(palavraNormalizada);
                boolean jaExiste = elementos.stream()
                        .anyMatch(e -> e.getId().equals(id) && e.getTitulo().equals(titulo));
                //Adiciona as info do elemento se ele nao estiver no cache
                if (!jaExiste) {
                    ElementoInfo novoElemento = new ElementoInfo(id, titulo);
                    double relevancia = calcularRelevancia(palavraNormalizada, texto, titulo);
                    //Define a relevancia da palavra no verbete
                    novoElemento.setRelevancia(relevancia);
                    elementos.add(novoElemento);
                }
            }
        }
    }



    public void buscar(String palavra) {

        palavra = palavra.toLowerCase().replaceAll("[^a-z0-9]", "");
        //Verifico se palavra esta no cache
        if (cache.containsKey(palavra)) {
            //Guardo as chaves (palavra buscada)
            List<ElementoInfo> resultados = cache.get(palavra);

            //Define um conjunto sem elementos duplicados
            Set<ElementoInfo> resultadosUnicos = new HashSet<>(resultados);

            //Nova lista para ordenacao dos verbetes
            List<ElementoInfo> resultadosOrdenados = new ArrayList<>(resultadosUnicos);
            //Ordenacao por relevancia
            resultadosOrdenados.sort((a, b) -> Double.compare(b.getRelevancia(), a.getRelevancia()));

            // printa os 5 primeiros resultados
            System.out.println("Resultados para: " + palavra);
            int limite = Math.min(resultadosOrdenados.size(), 5);
            for (int i = 0; i < limite; i++) {
                ElementoInfo info = resultadosOrdenados.get(i);
                System.out.println("ID: " + info.getId() + " | Título: " + info.getTitulo() +
                        " | Relevância: " + info.getRelevancia());
            }
        } else {
            System.out.println("Nenhum resultado encontrado para: " + palavra);
        }
    }

    private double calcularRelevancia(String palavra, String texto, String titulo) {
        palavra = palavra.toLowerCase().replaceAll("[^a-z0-9]", "");
        texto = texto.toLowerCase();
        titulo = titulo.toLowerCase();

        //Splita o texto
        String[] palavrasTexto = texto.split("\\s+");
        //Guarda o tamanho do texto
        int totalPalavras = palavrasTexto.length;


        String finalPalavra = palavra;
        //Conta as ocorrencias da palavra no texto
        //count retorna um long
        long ocorrencias = Arrays.stream(palavrasTexto)
                .filter(p -> p.equals(finalPalavra))
                .count();


        double relevancia = (double) ocorrencias / totalPalavras;

        //Se a palavra está no titulo incremento em 1
        if (titulo.contains(palavra)) {
            relevancia += 1.0;
        }

        return relevancia;
    }


}

