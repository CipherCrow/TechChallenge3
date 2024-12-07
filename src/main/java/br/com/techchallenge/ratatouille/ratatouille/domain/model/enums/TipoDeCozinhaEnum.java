package br.com.techchallenge.ratatouille.ratatouille.domain.model.enums;

public enum TipoDeCozinhaEnum{
    BRASILEIRA,
    JAPONESA,
    TAILANDESA,
    ITALIANA,
    CHINESA,
    INDIANA,
    MEXICANA,
    FRANCESA,
    GREGA,
    AMERICANA,
    HIBRIDA;

    public String getDescricao() {
        switch (this) {
            case BRASILEIRA:
                return "Culinária típica do Brasil, conhecida por pratos como feijoada e churrasco.";
            case JAPONESA:
                return "Culinária do Japão, famosa por sushi, sashimi e ramen.";
            case TAILANDESA:
                return "Culinária da Tailândia, com sabores intensos e equilibrados, como curry e pad thai.";
            case ITALIANA:
                return "Culinária italiana, reconhecida por pizzas, massas e gelatos.";
            case CHINESA:
                return "Culinária da China, com pratos como dumplings, peking duck e chow mein.";
            case INDIANA:
                return "Culinária da Índia, conhecida pelo uso de especiarias, curries e naan.";
            case MEXICANA:
                return "Culinária mexicana, com tacos, guacamole e quesadillas.";
            case FRANCESA:
                return "Culinária francesa, famosa por técnicas refinadas, queijos e vinhos.";
            case GREGA:
                return "Culinária grega, com pratos como moussaka, gyros e salada grega.";
            case AMERICANA:
                return "Culinária dos Estados Unidos, famosa por hambúrgueres, hot dogs e BBQ.";
            default:
                return "Descrição não disponível.";
        }
    }
}
