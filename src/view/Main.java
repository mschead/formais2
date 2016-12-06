package view;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Main {
  public static void main(String[] argv) throws Exception {
    DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);

    model.addColumn("Col1");
    model.addColumn("Col2");

    JFrame f = new JFrame();
    f.setSize(300, 300);
    f.add(new JScrollPane(table));
    f.setVisible(true);
  }
}
