
package swing;

import Model.Model_Menu;
import event.eventMenuSelected;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class ListMenu<E extends Object> extends JList<E>{
    
    private final DefaultListModel model;  
    private int selectedIndex = -1;
    private int overIndex=-1;
    
    private eventMenuSelected event;
    
    public void addEventMenuSelected(eventMenuSelected event){
        this.event = event;
    }
    
    public ListMenu(){
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    int index = locationToIndex(e.getPoint());
                    Object o = model.getElementAt(index);
                    if(o instanceof Model_Menu){
                        Model_Menu menu = (Model_Menu) o;
                        if (menu.getType() == Model_Menu.MenuType.MENU) {
                            selectedIndex = index;
                            if(event!=null){
                                event.selected(index);
                            }
                        }
                    }else{
                        selectedIndex = index;
                    }
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                overIndex=-1;
                repaint();
            }
             
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if(index != overIndex){
                    Object o = model.getElementAt(index);
                    if(o instanceof Model_Menu){
                        Model_Menu menu = (Model_Menu) o;
                        if(menu.getType()==Model_Menu.MenuType.MENU){
                            overIndex = index;
                        }else{
                            overIndex=-1;
                        }
                        repaint();
                    }
                }
            }
            
        });
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object o, int index, boolean Selected, boolean focus) {
                Model.Model_Menu data;
                if(o instanceof Model.Model_Menu){
                    data=(Model_Menu) o;
                }else{
                    data = new Model.Model_Menu("", o + "",Model.Model_Menu.MenuType.EMPTY);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                item.setOver(overIndex == index);
                return item;
            }
        };
    }
    
    public void addItem(Model_Menu data){
     model.addElement(data);
    }
    
}
