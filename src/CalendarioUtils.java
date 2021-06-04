import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class CalendarioUtils {

    public static LocalDate[][] montaCalendario(LocalDate data){
        LocalDate[][] calendario = new LocalDate[6][7];
        int[][] days_in_month = {
                {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
                {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
        };

        LocalDate primeiroDia = data.minusDays(data.getDayOfMonth()-1);
        int diaSemana = Integer.valueOf(DateTimeFormatter.ofPattern("e").format(primeiroDia))-1;
        int semenaMes = Integer.valueOf(DateTimeFormatter.ofPattern("W").format(primeiroDia))-1;

        int qtdeDiasMes = days_in_month[Year.isLeap(data.getYear())?1:0][data.getMonthValue()-1];

        int dia = 1;
        for(int i=semenaMes;i<6 && dia <= qtdeDiasMes;i++){
            for(int j=diaSemana;j<7 && dia <= qtdeDiasMes;j++){
                calendario[i][j] = LocalDate.of(data.getYear(),data.getMonth(),dia);
                dia += 1;
            }
            diaSemana=0;
        }

        return calendario;
    }

}
