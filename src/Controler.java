import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class Controler implements Initializable {


    @FXML
    private ComboBox<Month> cbMeses;

    @FXML
    private ComboBox<Year> cbAnos;

    @FXML
    private Label lbHoje;

    @FXML
    private GridPane gridCalendario;

    @FXML
    private ListView<EventoIntf> lvwHorarios;

    private ObservableList<Month> meses;
    private ObservableList<Year> anos;
    private ObservableList<EventoIntf> eventos;
    private ArrayList<CelulaData> celulas = new ArrayList<>();

    private Callback<LocalDate, List<EventoIntf>> getEventos;
    private Callback<LocalDate, List<EventoIntf>> getEventosDia;

    private CelulaData diaSelecionado;

    public Controler(){
        meses = FXCollections.observableArrayList();
        anos = FXCollections.observableArrayList();
        eventos = FXCollections.observableArrayList();
    }

    public void setGetEventos(Callback<LocalDate, List<EventoIntf>> getEventos){
        this.getEventos = getEventos;
    }

    public void setGetEventosDia(Callback<LocalDate, List<EventoIntf>> getEventosDia) {
        this.getEventosDia = getEventosDia;
    }


    private void inicializaMeses(){
        for(int i=1;i<=12;i++){
            meses.add(Month.of(i));
        }
    }

    private void inicializaAnos(){
        int ano = LocalDateTime.now().getYear();
        for(int i=ano-5;i<=ano+1;i++){
            anos.add(Year.of(i));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializaAnos();
        inicializaMeses();

        LocalDate hoje = LocalDate.now();

        int ano = hoje.getYear();
        Month mes = hoje.getMonth();

        cbAnos.setItems(anos);
        cbAnos.setOnAction(evt -> {
            LocalDate data = LocalDate.of(cbAnos.getValue().getValue(),cbMeses.getValue(),1);
            System.out.println(data);
            preencheCalendario(data);
        });
        cbAnos.getSelectionModel().select(Year.of(ano));

        cbMeses.setItems(meses);
        cbMeses.setCellFactory(new Callback<ListView<Month>, ListCell<Month>>() {
            @Override
            public ListCell<Month> call(ListView<Month> monthListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(Month month, boolean b) {
                        super.updateItem(month, b);
                        if(month != null){
                            setText(month.getDisplayName(TextStyle.FULL,Locale.forLanguageTag("pt-br")));
                        }
                    }
                };
            }
        });
        cbMeses.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Month month, boolean b) {
                super.updateItem(month, b);
                if(month != null){
                    setText(month.getDisplayName(TextStyle.FULL,Locale.forLanguageTag("pt-br")));
                }
            }
        });

        cbMeses.setOnAction(evt -> {
            LocalDate data = LocalDate.of(cbAnos.getValue().getValue(),cbMeses.getValue(),1);
            System.out.println(data);
            preencheCalendario(data);
        });
        cbMeses.getSelectionModel().select(mes);

        lvwHorarios.setCellFactory(new Callback<ListView<EventoIntf>, ListCell<EventoIntf>>() {
            @Override
            public ListCell<EventoIntf> call(ListView<EventoIntf> eventoIntfListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(EventoIntf eventoIntf, boolean b) {
                            super.updateItem(eventoIntf, b);
                            if(eventoIntf != null){
                                setText(eventoIntf.getHora()+" "+eventoIntf.getDescricao());
                            }else{
                                setText("");
                            }

                    }
                };
            }
        });

        lvwHorarios.setItems(eventos);

        inicializaGridCalendario();
        preencheCalendario(hoje);

        diaSelecionado = celulas.stream().filter((celulaData) -> celulaData.getData()!= null && celulaData.getData().equals(hoje)
        ).findFirst().get();

        selecionaDia(diaSelecionado);
    }

    private void ajustaLabelData(LocalDate data){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("EEEE - dd/MM/yyyy");

        lbHoje.setText(df.format(data).toUpperCase());
    }

    private void inicializaGridCalendario(){
        int lin=0;
        String[] diasSemana = {"Dom.","Seg.","Ter.","Qua.","Qui.","Sex.","Sab."};

        for(int col=0;col<diasSemana.length;col++){
            HBox hbox = new HBox(new Text(diasSemana[col]));
            hbox.setAlignment(Pos.CENTER);
            hbox.getStyleClass().addAll("diaSemana","dia");
            gridCalendario.add(hbox,col,lin);
        }
        for(int i=1;i<7;i++){
            for(int j=0;j<7;j++){
                CelulaData celula = new CelulaData(Pos.CENTER_LEFT);
                celula.setOnMouseClicked(evt -> seleciona(evt));

                celulas.add(celula);
                gridCalendario.add(celula,j,i);

            }
        }
    }

    private void selecionaDia(CelulaData dia){
        if(diaSelecionado != null){
            diaSelecionado.deseleciona();
            if(diaSelecionado.getData() != null){
                diaSelecionado = dia;
                diaSelecionado.seleciona();

                ajustaLabelData(diaSelecionado.getData());
                //List<EventoIntf> lista = this.getEventos.call(diaSelecionado.getData());
                //eventos.clear();
                //eventos.addAll(lista);
            }
        }


    }


    private void seleciona(MouseEvent evt){
        selecionaDia((CelulaData) evt.getSource());

    }

    /*Ajusta o texto das células do gridpane de acordo com o dia do calendario*/
    private void preencheCalendario(LocalDate data){
        LocalDate[][] calendario = CalendarioUtils.montaCalendario(data);

        LocalDate hoje = LocalDate.now();

        for(int i=0;i<calendario.length;i++){
            for(int j=0;j<calendario[i].length;j++){
                CelulaData celula = (CelulaData) gridCalendario.getChildren().get((8+(7*i+j)));

                celula.limpaEstilos();

                LocalDate dia = calendario[i][j];


                if(dia != null){
                    /*ajusta a data da célula*/
                    celula.setData(dia);
                    celula.ajustaEstilos();
                }else{
                    /*caso não tenha data no calenário, deixar em branco*/
                    celula.clear();
                }


            }
        }

    }

    @FXML
    private void mudaMes(Event evt){
        if(((Button)evt.getSource()).getText().equals("-")){
            cbMeses.getSelectionModel().selectPrevious();
        }else{
            cbMeses.getSelectionModel().selectNext();
        }

    }

    @FXML
    private void mudaAno(Event evt){
        if(((Button)evt.getSource()).getText().equals("-")){
            cbAnos.getSelectionModel().selectPrevious();
        }else{
            cbAnos.getSelectionModel().selectNext();
        }

    }



}
