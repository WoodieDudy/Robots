package robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import robots.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exit();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected JMenuItem generateJMenuItem(String title, int hotKey, ActionListener action) {
        JMenuItem jmenuItem = new JMenuItem(title, hotKey);
        jmenuItem.addActionListener(action);
        return jmenuItem;
    }

    protected JMenu generateJMenu(String title, String description, int hotKey) {
        JMenu jMenu = new JMenu(title);
        jMenu.setMnemonic(hotKey);
        jMenu.getAccessibleContext().setAccessibleDescription(description);
        return jMenu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = generateJMenu("Режим отображения", "Управление режимом отображения приложения", KeyEvent.VK_V);
        lookAndFeelMenu.add(generateJMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = generateJMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);
        testMenu.add(generateJMenuItem("Сообщение в лог", KeyEvent.VK_S,
            (event) -> {Logger.debug("Новая строка");}
        ));
        return testMenu;
    }

    private JMenu createExitMenu() {
        JMenu exitMenu = generateJMenu("Выход", "Выход из приложения", KeyEvent.VK_Q);
        exitMenu.add(generateJMenuItem("Выход из приложения", KeyEvent.VK_P,
            (event) -> {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Вы действительно хотите выйти?",
                        "Подтверждение выхода",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (result == JOptionPane.YES_OPTION) exit();
            }
        ));
        return exitMenu;
    }

    protected JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }

    protected void exit() {
        System.exit(0);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}