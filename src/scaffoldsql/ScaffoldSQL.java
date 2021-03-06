package scaffoldsql;

import java.util.Scanner;

/**
 *
 * @author juliand
 */
public class ScaffoldSQL {


    public static void main(String[] args) {
        try {
            int op;
            Scanner in = new Scanner(System.in);
            do {
                System.out.println("==================SQLSCaf=========================");
                System.out.println("===============SQL to SCAFFOLD====================");
                System.out.println("1. Ver Scaffold desde SQL.");
                System.out.println("0. Salir.");
                op = Integer.parseInt(in.nextLine());
                if (op == 0) {
                    System.out.println("Saliendo de SQLSCaf.");
                    return;
                } else if (op == 1) {
                    long cant_scaffold = 0;
                    System.out.println("Ingrese su Script SQL:");
                    while (true) {
                        String sql, scaf = "rails g scaffold ";
                        sql = in.nextLine().trim();
                        if (!sql.isEmpty()) {
                            if (Comparar_cadena_Starts(sql, "CREATE TABLE")) {
                                //Buscar nombre de la tabla
                                String nombreTabla = BuscarTabla(sql);
                                scaf += nombreTabla;
                                //Fin de busqueda de la tabla

                                //Buscando los atributos de la tabla
                                while (!sql.endsWith(";")) {
                                    sql = in.nextLine().trim();
//                                    System.out.println("Procesando " + sql);

                                    //Buscar nombre de los atributos                                   
                                    if (sql.charAt(0) == '`' || sql.charAt(0) == '"') {
                                        int segundoIndex = sql.substring(1).indexOf("`");
                                        String nombreAtributo = sql.substring(1, segundoIndex + 1);
//                                      System.out.println("Nombre atributo: " + nombreAtributo);

                                        //Buscar tipo de atributo
                                        String tipoAtributo
                                                = sql.substring(segundoIndex + 2, sql.length()).trim().split(" ")[0];
//                                      System.out.println("Tipo de atributo: " + tipoAtributo);

                                        scaf = scaf.concat(nombreAtributo + ":" + Traducir(tipoAtributo) + " ");
                                    } 
                                    /*else if (Comparar_cadena_Starts(sql, "REFERENCES")) {
                                        // Sacando nombre de la tabla a la que se hace referencia
                                        System.out.println("Se encontro referencia");
                                        int index_bd_o_tabla1;
                                        int index_bd_o_tabla2;
                                        String ref = "NO HAY REFERENCIA";
                                        if (sql.contains("`.`")) {
                                            index_bd_o_tabla1 = sql.indexOf(".`");
                                            if (index_bd_o_tabla1 > 0) {
                                                index_bd_o_tabla2
                                                        = sql.substring(index_bd_o_tabla1 + 2, sql.length()).indexOf("`");
                                                if (index_bd_o_tabla2 > 0) {
                                                    ref = sql.substring(index_bd_o_tabla1, index_bd_o_tabla2);
                                                }
                                            }
                                        } else {
                                            index_bd_o_tabla1 = sql.indexOf("`");
                                            if (index_bd_o_tabla1 > 0) {
                                                index_bd_o_tabla2
                                                        = sql.substring(index_bd_o_tabla1, sql.length()).indexOf("`");
                                                if (index_bd_o_tabla2 > 0) {
                                                    ref = sql.substring(index_bd_o_tabla1, index_bd_o_tabla2);
                                                }
                                            }
                                        }
                                        System.out.println("Nombre de la referencia: [" + ref + "]");
                                    }*/
                                }
                                System.out.println("-----------------------------------------");
                                System.out.println("Scaffold " + ++cant_scaffold + " + " + nombreTabla + ": [" + scaf + "]");
//                              System.out.println("Fin del procesado en la tabla " + nombreTabla);
                            }
                        }
                    }
                }
            } while (true);
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

    private static boolean Comparar_cadena_Starts(
            String aComparar,
            String SQL) {
        return aComparar.toUpperCase().startsWith(SQL.toUpperCase());
    }

    private static String Traducir(String tipo_columna) {
        String[][] pal = {
            {"VARCHAR", "string"},
            {"INT", "integer"}
        };
        for (String[] pal1 : pal) {
            if (pal1[0].toUpperCase().equals(tipo_columna.toUpperCase())) {
                return pal1[1];
            }
        }
        return tipo_columna;
    }

    private static String BuscarTabla(String sql) throws Exception {
        String nombreTabla = "";
        int posInicialTabla = sql.indexOf("`.`");
        if (posInicialTabla > -1) {
            for (int i = posInicialTabla + 3; i < sql.length(); i++) {
                if (sql.charAt(i) != '`'
                        && sql.charAt(i) != ' ') {
                    nombreTabla += sql.charAt(i);
                } else {
                    break;
                }
            }
        } else {
            posInicialTabla = sql.indexOf("`");
            for (int i = posInicialTabla + 1; i < sql.length(); i++) {
                if (sql.charAt(i) != '`'
                        && sql.charAt(i) != ' ') {
                    nombreTabla += sql.charAt(i);
                } else {
                    break;
                }
            }
        }
        if (nombreTabla.length() > 0) {
            return nombreTabla + " ";
        } else {
            throw new Exception("ERROR AL ENCONTRAR EL NOMBRE DE LA TABLA.\nVerifique su sintaxis..");
        }
    }
}
