package procesosbloqueo;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ProcesosBloqueo extends JFrame implements Runnable, ActionListener {

    JScrollPane scrollPane = new JScrollPane();
    JScrollPane scrollPane1 = new JScrollPane();

    JScrollPane scrollPane2 = new JScrollPane();
    JScrollPane scrollPane3 = new JScrollPane();

    JScrollPane scrollPane4 = new JScrollPane();
    JScrollPane scrollPane5 = new JScrollPane();

    JLabel label1 = new JLabel("Nombre del proceso: ");
    JLabel label3 = new JLabel("Proceso en ejecucion: Ninguno");
    JLabel label4 = new JLabel("Tiempo: ");
    JLabel label5 = new JLabel("Tabla de procesos:");
    JLabel label6 = new JLabel("Diagrama de Gant:");
    JLabel label7 = new JLabel("Tabla de Bloqueados:");
    JLabel label8 = new JLabel("Rafaga restante del proceso: 0");
    JLabel label9 = new JLabel("Rafaga");

    JButton botonIngresar = new JButton("Ingresar proceso");
    JButton botonIniciar = new JButton("Iniciar ejecucion");


    JTextField tfNombre = new JTextField("P1");
    JTextField tfrafaga = new JTextField("");

    JTextField[][] tabla = new JTextField[100][8];
    JTextField[][] tablaBloqueados = new JTextField[100][4];
    JLabel[][] diagrama = new JLabel[40][100];

    ListaCircular cola = new ListaCircular();

    Nodo nodoEjecutado;

    int filas = 0, rafagaTemporal;
    int tiempoGlobal = 0;
    int coorX = 0, coorY = 1;

    Thread procesos;

    public static void main(String[] args) throws InterruptedException {

        ProcesosBloqueo pb = new ProcesosBloqueo();
        pb.setBounds(0, 0, 1200, 730);
        pb.setTitle("SRTF ");
        pb.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pb.setVisible(true);

    }

    ProcesosBloqueo() {

        Container c = getContentPane();
        c.setLayout(null);
        this.getContentPane().setBackground(Color.BLUE);

        c.add(label1);
        c.add(label3);
        c.add(label4);
        c.add(label5);
        c.add(label6);
        c.add(label7);
        c.add(label8);
        c.add(label9);

        c.add(tfrafaga);
        c.add(scrollPane1);
        c.add(scrollPane3);
        c.add(scrollPane5);

        c.add(botonIngresar);
        c.add(botonIniciar);

        

        c.add(tfNombre);


        label1.setBounds(800, 70, 300, 20);
        label3.setBounds(800, 250, 300, 20);
        label4.setBounds(1020, 250, 300, 20);
        label5.setBounds(50, 20, 300, 20);
        label6.setBounds(50, 280, 300, 20);
        label7.setBounds(800, 280, 300, 20);
        label8.setBounds(800, 265, 300, 20);
        label9.setBounds(800, 130, 300, 20);

        scrollPane.setBounds(50, 40, 2500, 2500);
        scrollPane.setPreferredSize(new Dimension(2500, 2500));
        scrollPane.setBackground(Color.LIGHT_GRAY);

        scrollPane1.setBounds(50, 40, 745, 230);
        scrollPane1.setPreferredSize(new Dimension(1150, 400));
        scrollPane1.setBackground(Color.LIGHT_GRAY);

        scrollPane2.setBounds(50, 300, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));
        scrollPane2.setBackground(Color.LIGHT_GRAY);

        scrollPane3.setBounds(50, 300, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(1150, 400));
        scrollPane3.setBackground(Color.WHITE);

        scrollPane2.setBounds(50, 300, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));
        scrollPane2.setBackground(Color.WHITE);

        scrollPane3.setBounds(50, 300, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(700, 350));
        scrollPane3.setBackground(Color.WHITE);

        scrollPane4.setBounds(800, 300, 500, 1000);
        scrollPane4.setPreferredSize(new Dimension(500, 1000));
        scrollPane4.setBackground(Color.WHITE);

        scrollPane5.setBounds(800, 300, 350, 350);
        scrollPane5.setPreferredSize(new Dimension(350, 350));
        scrollPane5.setBackground(Color.WHITE);

        tfNombre.setBounds(930, 70, 70, 20);
        tfrafaga.setBounds(930, 130, 70, 20);

        botonIngresar.addActionListener(this);
        botonIngresar.setBounds(1010, 100, 150, 40);
        botonIngresar.setBackground(Color.LIGHT_GRAY);

        botonIniciar.addActionListener(this);
        botonIniciar.setBounds(1010, 150, 150, 40);
        botonIniciar.setBackground(Color.LIGHT_GRAY);


    }

    public void dibujarTabla(String nombre, int rafaga, int tiempo) {

        scrollPane.removeAll();

        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");
        JLabel texto5 = new JLabel("T. comienzo");
        JLabel texto6 = new JLabel("T. final");
        JLabel texto7 = new JLabel("T. retorno");
        JLabel texto8 = new JLabel("T. espera");
        JLabel texto9 = new JLabel("Estado");

        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        texto5.setBounds(260, 20, 150, 20);
        texto6.setBounds(340, 20, 150, 20);
        texto7.setBounds(420, 20, 150, 20);
        texto8.setBounds(500, 20, 150, 20);
        texto9.setBounds(580, 20, 150, 20);
     

        scrollPane.add(texto1);
        scrollPane.add(texto2);
        scrollPane.add(texto3);
        scrollPane.add(texto5);
        scrollPane.add(texto6);
        scrollPane.add(texto7);
        scrollPane.add(texto8);
        scrollPane.add(texto9);

        for (int i = 0; i < filas; i++) {

            for (int k = 0; k < 8; k++) {

                if (tabla[i][k] != null) {
                    scrollPane.add(tabla[i][k]);
                } else {

                    tabla[i][k] = new JTextField("-");
                    tabla[i][k].setBounds(20 + (k * 80), 40 + (i * 25), 70, 20);

                    if (k == 7) {
                        tabla[i][k] = new JTextField("Espera");
                        tabla[i][k].setBounds(20 + (k * 80), 40 + (i * 25), 70, 20);
                    }
                    scrollPane.add(tabla[i][k]);
                }

            }

        }

        tabla[filas - 1][0].setText(nombre);
        tabla[filas - 1][1].setText(Integer.toString(tiempo));
        tabla[filas - 1][2].setText(Integer.toString(rafaga));

        scrollPane.repaint();
        scrollPane1.setViewportView(scrollPane);

    }

    public void llenarBloqueados() {

        scrollPane4.removeAll();

        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");

        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);


        scrollPane4.add(texto1);
        scrollPane4.add(texto2);
        scrollPane4.add(texto3);
  
        ordenarRafagas();
        if (cola.getCabeza() != null) {

            Nodo temp = cola.getCabeza().getSiguiente();

            for (int i = 0; i < cola.getTamaño()-1; i++) {

                for (int j = 0; j < 3; j++) {

                    tablaBloqueados[i][j] = new JTextField("");
                    tablaBloqueados[i][j].setBounds(20 + (j * 80), 40 + (i * 25), 70, 20);

                    scrollPane4.add(tablaBloqueados[i][j]);

                }

                tablaBloqueados[i][0].setText(temp.getLlave());
                tablaBloqueados[i][1].setText(Integer.toString(temp.getLlegada()));
                tablaBloqueados[i][2].setText(Integer.toString(temp.getRafaga()));


                temp = temp.getSiguiente();

            }
        }

        scrollPane4.repaint();
        scrollPane5.setViewportView(scrollPane4);

    }

    public void llenarRestante() {

        if (nodoEjecutado.getRafaga() == 0) {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Terminado");
        } else {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Bloqueado");
        }

        tabla[nodoEjecutado.getIndice() - 1][3].setText(Integer.toString(nodoEjecutado.getComienzo()));
        tabla[nodoEjecutado.getIndice() - 1][4].setText(Integer.toString(nodoEjecutado.getFinalizacion()));
        tabla[nodoEjecutado.getIndice() - 1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));
        tabla[nodoEjecutado.getIndice() - 1][6].setText(Integer.toString(nodoEjecutado.getComienzo() - nodoEjecutado.getLlegada()));

    }

    public void dibujarEsperas() {

        JLabel img2 = new JLabel();

        ImageIcon imgIcon2 = new ImageIcon(getClass().getResource("barraEspera.png"));

        Image imgEscalada2 = imgIcon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado2 = new ImageIcon(imgEscalada2);

        for (int i = coorX - 1; i >= nodoEjecutado.getLlegada(); i--) {

            diagrama[coorY][i + 1] = new JLabel();
            diagrama[coorY][i + 1].setBounds(40 + (i * 20), 20 + (coorY * 20), 20, 20);
            diagrama[coorY][i + 1].setIcon(iconoEscalado2);

            scrollPane2.add(diagrama[coorY][i + 1]);

        }

    }

    public void dibujarDiagrama(String nombre, int coorX, int coorY) {

        scrollPane2.removeAll();

        for (int i = 0; i < 100; i++) {

            diagrama[0][i] = new JLabel(Integer.toString(i));
            diagrama[0][i].setBounds(40 + (i * 20), 20, 20, 20);

            scrollPane2.add(diagrama[0][i]);

        }

        diagrama[coorY][0] = new JLabel("  " + nombre);
        diagrama[coorY][0].setBounds(0, 20 + (coorY * 20), 50, 20);

        scrollPane2.add(diagrama[coorY][0]);

        JLabel img = new JLabel();

        ImageIcon imgIcon = new ImageIcon(getClass().getResource("barra.png"));

        Image imgEscalada = imgIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);

        for (int i = 1; i < coorY + 1; i++) {

            for (int j = 0; j < coorX + 1; j++) {

                if (diagrama[i][j] != null) {

                    scrollPane2.add(diagrama[i][j]);

                }

            }

        }

        diagrama[coorY][coorX + 1] = new JLabel();
        diagrama[coorY][coorX + 1].setBounds(40 + (coorX * 20), 20 + (coorY * 20), 20, 20);
        diagrama[coorY][coorX + 1].setIcon(iconoEscalado);

        scrollPane2.add(diagrama[coorY][coorX + 1]);

        scrollPane2.repaint();
        scrollPane3.setViewportView(scrollPane2);

    }

    
    public void ordenarRafagas() {

        int movimientos = 0;
        int contador = 0;

        Nodo temp = cola.getCabeza().getSiguiente();

        int menorRafa = cola.getCabeza().getRafaga();

        while (!(temp.equals(cola.getCabeza()))) {

            contador++;

            if (temp.getRafaga() < menorRafa) {
                menorRafa = temp.getRafaga();
                movimientos = contador;

            }

            temp = temp.getSiguiente();

        }
        for (int i = 0; i < movimientos; i++) {
            cola.intercambiar(cola.getCabeza());
        }
    }

    public void ingresar(String nombre,int rafaga, int tiempo, int filas) {
        cola.insertar(nombre, rafaga, tiempo, filas);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == botonIngresar) {
            
            filas++;
            
            String nombre = tfNombre.getText();
            String valorraf = tfrafaga.getText();
            int varafa = Integer.parseInt(valorraf);
            rafagaTemporal = varafa;

            ingresar(nombre, rafagaTemporal, tiempoGlobal, filas);
            dibujarTabla(nombre,  rafagaTemporal, tiempoGlobal);
            tfNombre.setText("P" + (filas + 1));
            try {
                if(rafagaTemporal<nodoEjecutado.getRafaga()){
                
                        if (nodoEjecutado.getRafaga() != 0) {
                           filas++;
                            ingresar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas);
                            dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                            nodoEjecutado.setFinalizacion(tiempoGlobal);
                            llenarRestante();
                            cola.eliminar(cola.getCabeza());
                            ordenarRafagas();
                            llenarBloqueados();
                            nodoEjecutado = cola.getCabeza();
                            coorY++;
                            nodoEjecutado.setComienzo(tiempoGlobal);
                            dibujarEsperas();
                        }
                }
            } catch (Exception e1) {
                 
            }
            
        } else if (e.getSource() == botonIniciar) {

            procesos = new Thread(this);
            procesos.start();

        }
    }

    @Override
    public void run() {

        try {
            while (cola.getTamaño() != 0) {

                ordenarRafagas();
                llenarBloqueados();
                nodoEjecutado = cola.getCabeza();
                nodoEjecutado.setComienzo(tiempoGlobal);

                dibujarEsperas();
                
                while (nodoEjecutado.getRafaga() > 0) {
                    tabla[nodoEjecutado.getIndice() - 1][7].setText("Ejecutando");

                    nodoEjecutado.setRafaga(nodoEjecutado.getRafaga() - 1);
                  
                    label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                    label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                    label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());

                    dibujarDiagrama(nodoEjecutado.getLlave(), coorX, coorY);
                    
                    tiempoGlobal++;
                    coorX++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProcesosBloqueo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                nodoEjecutado.setFinalizacion(tiempoGlobal);
                llenarRestante();
                cola.eliminar(cola.getCabeza());
                ordenarRafagas();
                llenarBloqueados();
                
                coorY++;

            }
            
            label3.setText("Proceso en ejecucion: Ninguno");
            scrollPane4.removeAll();
            scrollPane4.repaint();
        } catch (Exception e) {

            System.out.print("Error");

        }

    }
}
