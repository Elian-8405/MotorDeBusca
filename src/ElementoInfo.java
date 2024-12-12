/**
 * Clase utilizada para guardar informações dos verbetes
 *
 *
 *
 * */

public class ElementoInfo {
    private final String id;
    private final String titulo;
    private double relevancia;
    /**
     * Construtor de ElementoInfo
     *
     * @param id
     * @param titulo
     * */
    public ElementoInfo(String id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.relevancia = 0.0;
    }

    //Getters e Setters

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getRelevancia() {
        return relevancia;
    }

    public void setRelevancia(double relevancia) {
        this.relevancia = relevancia;
    }
}
