package control;

import connection.ConnSQL;
import view.VLogin;

import java.util.List;

@SuppressWarnings("ALL")
public class Control {

    ConnSQL con; //Instancia del objeto de conexión con la base de datos

    public Control() {
        con = new ConnSQL(); //Creación de la conexión con la base de datos SQL en mySQL
        new VLogin(this); //
    }

    //Permite conocer si existe o no un usuario con un nombre dado
    private boolean existUser(String user, List<String> names) {
        for (int i = 0; i < names.size(); i++) {
            if (user.equals(names.get(i))) {
                return true;
            }
        }
        return false;
    }

    /*Con un nombre de usuario y una contraseña se hace el cruce en la base de datos con el fin de encontrar
    coincidencias respecto al nombre de usuario y su respectiva contraseña*/
    public boolean login(String user, String pass) {
        //Escribimos la sentencia SQL y la columna especifica que deseamos obtener
        List<String> names = con.getData("SELECT * FROM usuario", "Username");
        //Se obtiene la lista de usuarios de la bd

        if (!existUser(user, names)) {
            System.out.println("El usuario no existe");
            return false;
        }

        if (pass.equals(con.getData("SELECT * FROM usuario where Username = " +
                "'" + user + "'", "Password").get(0))) {
            System.out.println("Bienvenido");
            return true;
        }
        System.out.println("Contraseña incorrecta");
        return false;
    }

    /*Similar al anterior con un codigo se hace el cruce en la base de datos con el fin de encontrar
    coincidencias respecto al estudiante identificado con el codigo ingresado y sus creditos disponibles*/
    public String getCredits(String code) {
        List<String> codes = con.getData("SELECT * FROM estudiante", "Codigo");

        if (!existUser(code, codes)) {
            return "Estudiante no encontrado";
        } else {
            return "Cantidad de creditos: " + con.getData("SELECT * FROM estudiante where Codigo = '"
                    + code + "'", "Cant_Creditos").get(0);
        }
    }

    public String getBuyCredEst(String code){
        return con.getOneColumn("SELECT sum(Cantidad) FROM registro where Tipo=2" +
                " and Estudiante_ID=(SELECT Estudiante_ID from estudiante where Codigo='"+code+"')");
    }

    public String getConsumeCredEst(String code){
        String one = con.getOneColumn("SELECT Estudiante_ID from estudiante where Codigo='"+code+"'");
        return con.getOneColumn("SELECT sum(Cantidad) FROM registro where Tipo=1" +
                " and Estudiante_ID='"+one+"'");
    }

    public List<String> getData(String code){
        return con.getData("SELECT * FROM estudiante where Codigo='"+code+"'");
    }

    public void addCredits(String code, String quantity) {
        int c = Integer.parseInt(con.getData("SELECT * FROM estudiante where Codigo='" + code + "' ",
                "Cant_Creditos").get(0)) + Integer.parseInt(quantity);
        con.setData("UPDATE estudiante set Cant_Creditos='" + c + "' where Codigo='" + code + "'");
    }

    public List<String> chargeMenu(String date){
        return con.getDataMenu("SELECT * FROM menu where Fecha='"+date+"'");
    }


}
