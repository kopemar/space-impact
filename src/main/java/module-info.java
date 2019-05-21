module cz.cvut.fel {
    requires javafx.controls;
    requires javafx.base;
    requires java.logging;
    requires gson;
    requires java.sql;

    opens cz.cvut.fel to javafx.graphics, javafx.base, javafx.controls, gson;
    exports cz.cvut.fel;
    exports cz.cvut.fel.Managers;
    exports cz.cvut.fel.Model;

    opens cz.cvut.fel.Model to gson;
}