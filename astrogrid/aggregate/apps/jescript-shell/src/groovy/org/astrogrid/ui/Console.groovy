package org.astrogrid.ui

import groovy.swing.SwingBuilder
import groovy.ui.*
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.util.EventObject
import groovy.lang.GroovyShell;
import java.net.URL;
import javax.swing.KeyStroke
import javax.swing.JSplitPane
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.text.StyleContext
import org.astrogrid.scripting.Toolbox
import org.astrogrid.community.beans.v1.*
import org.astrogrid.store.Ivorn
import org.astrogrid.community.User

import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Groovy Swing console.
 *
 * @author Danno Ferrin
 * @modified Noel Winstanley -- integration with astorogrid.
 */
class Console extends ConsoleSupport {

    frame
    swing
    textArea
    outputArea
    scriptList
    scriptFile
    codeBase

    private URL helpURL = new URL("http://www.astrogrid.org/maven/docs/HEAD/jes/index.html");
    private boolean dirty
    private GroovyShell shell
    private int counter = 0;

    static void main(args) {
        MetaClass.setUseReflection(true)
        console = new Console()
        console.run()
    }

    void login() {
        creds = new Credentials();
        acc = new Account()
        group = new Group()
        group.name = "shell-users"
        acc.name = JOptionPane.showInputDialog("Usename:")
        // password not used.
        password = JOptionPane.showInputDialog("Password:")
        acc.community = JOptionPane.showInputDialog("Community:","org.astrogrid")
        group.community = acc.community

        creds.account = acc
        creds.group = group
        creds.setSecurityToken("dummy")
        createBasicScriptBinding(new Toolbox(),creds)
    }

    void createBasicScriptBinding(Toolbox toolbox2, Credentials credentials) {
        shell = new GroovyShell();
        shell.setProperty("astrogrid",toolbox2)

        shell.setProperty("account",credentials.account)
        String name = credentials.account.name
        String community = credentials.account.community
        User u = new User(name,community,credentials.group.name,credentials.securityToken)
        shell.setProperty("user",u)

        try {
            shell.setProperty("userIvorn",new Ivorn("ivo://" + community + "/" +  name));
        } catch (Exception e) {
            logger.error("URISyntaxException when creating userIvorn.",e);
        }
        shell.setProperty("homeIvorn",new Ivorn(community,name,name + "/"));

    }


    void determineCodeBase() {
    /** rats - was cool, but not the way to do it.
      will re-use later to open browser for documentation
        managerClass = Class.forName('javax.jnlp.ServiceManager');
        if (managerClass != null) {
           println("Found service manager");
           try {
                lookupMethod = managerClass.getMethods().toList().find {it.getName() == 'lookup' }
                basicService = lookupMethod.invoke(null,new Object[]{'javax.jnlp.BasicService'})
                this.codeBase = basicService.getCodeBase().toString()
                println(this.codeBase)
           } catch (Exception e) {
           println("basic service unavailable");
           e.printStackTrace();
           }
        }
     */
     // doesn't work either - but looks right. pity.
     protectionDomain = this.getClass().getProtectionDomain()
     println(protectionDomain)
     this.codeBase = protectionDomain.getCodeSource().getLocation().toString()
     println(this.codeBase)
    }

    void run() {
        this.determineCodeBase();
        this.login()
        scriptList = []
        // if menu modifier is two keys we are out of luck as the javadocs
        // incicates it returns "Control+Shift" instead of "Control Shift"
        menuModifier = KeyEvent.getKeyModifiersText(
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
            .toLowerCase() + ' '

        swing = new SwingBuilder()
        frame = swing.frame(
                title:'JEScript Console',
                location:[100,100],
                size:[800,400],
                defaultCloseOperation:javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE) {
            newAction = action(
                name:'New',
                closure: fileNew,
                mnemonic: 'N',
                accelerator: menuModifier + 'N'
            )
            openAction = action(
                name:'Open',
                closure: fileOpen,
                mnemonic: 'O',
                accelerator: menuModifier + 'O'
            )
            saveAction = action(
                name:'Save',
                closure: fileSave,
                mnemonic: 'S',
                accelerator: menuModifier + 'S'
            )
            exitAction = action(name:'Exit', closure: exit, mnemonic: 'x')
            runAction = action(
                name:'Run',
                closure: runScript,
                mnemonic: 'R',
                keyStroke: 'ctrl ENTER',
                accelerator: 'ctrl R'
            )
            helpAction = action(
                name:'Help',
                closure: displayHelp,
                mnemonic: 'H',
                keyStroke: 'F1'
            )
            aboutAction = action(name:'About', closure: showAbout, mnemonic: 'A')
            menuBar {
                menu(text:'File', mnemonic:0x46) {
                    menuItem() { action(newAction) }
                    menuItem() { action(openAction) }
                    separator()
                    menuItem() { action(saveAction) }
                    separator()
                    menuItem() { action(exitAction) }
                }
                menu(text:'Actions', mnemonic: 'A') {
                    menuItem() { action(runAction) }
                }
                menu(text:'Help', mnemonic: 'H') {
                    menuItem() { action(helpAction) }
                    menuItem() { action(aboutAction) }
                }
            }
            splitPane(orientation:JSplitPane.VERTICAL_SPLIT, resizeWeight:0.50F) {
                scrollPane {
                    outputArea = textPane(editable:false)
                    addStylesToDocument(outputArea)
                }
                scrollPane {
                    textArea = textArea() { action(runAction) }

                }
            }
        }

        frame.setSize(500,400)

        // add listeners
        frame.windowClosing = exit
        textArea.document.undoableEditHappened = { setDirty(true) }

        frame.show()
        SwingUtilities.invokeLater({textArea.requestFocus()});
    }

    void setDirty(boolean newDirty) {
        dirty = newDirty
        updateTitle()
    }

    updateTitle() {
        if (scriptFile != null) {
            frame.title = scriptFile.name + (dirty?" * ":"") + " - GroovyConsole"
        } else {
            frame.title = "GroovyConsole"
        }
    }

    selectFilename(name = "Open") {
        fc = new JFileChooser()
        fc.fileSelectionMode = JFileChooser.FILES_ONLY
        if (fc.showDialog(frame, name) == JFileChooser.APPROVE_OPTION) {
            return fc.selectedFile
        } else {
            return null
        }
    }

    fileNew(EventObject evt = null) {
      (new Console()).run()
    }

    fileOpen(EventObject evt = null) {
        scriptFile = selectFilename();
        if (scriptFile != null) {
            textArea.text = scriptFile.readLines().join('\n');
            setDirty(false)
            textArea.caretPosition = 0
        }
    }

    boolean fileSave(EventObject evt = null) {
        if (scriptFile == null) {
            scriptFile = selectFilename("Save");
        }
        if (scriptFile != null) {
            scriptFile.write(textArea.text)
            setDirty(false);
            return true
        } else {
            return false
        }
    }

    runScript(EventObject evt = null) {
        text = textArea.getText()
        scriptList.add(text)

        doc = outputArea.getStyledDocument();

        promptStyle = getPromptStyle()
        commandStyle = getCommandStyle()
        outputStyle = getOutputStyle()

        for (line in text.tokenize("\n")) {
            doc.insertString(doc.getLength(), "\ngroovy> ", promptStyle)
            doc.insertString(doc.getLength(), line, commandStyle)
        }

        String name = "Script" + this.counter++
        try {
            codeSource = new GroovyCodeSource(text,name,this.codeBase)
            answer = shell.evaluate(codeSource)
        } catch (Exception e) {
            handleException(text, e)
            return null
        }

        output = "\n" + InvokerHelper.inspect(answer)
        doc.insertString(doc.getLength(), output, outputStyle)

        //println("Variables: " + shell.context.variables)

        if (scriptFile == null) {
            textArea.setText(null);
        }
    }

    protected void handleException(String text, Exception e) {
        pane = swing.optionPane(message:'Error: ' + e.getMessage() + '\nafter compiling: ' + text)
        dialog = pane.createDialog(frame, 'Compile error')
        dialog.show()
        e.printStackTrace()
    }

    displayHelp(EventObject evt = null) {
        managerClass = Class.forName('javax.jnlp.ServiceManager');
        if (managerClass != null) {
                lookupMethod = managerClass.getMethods().toList().find {it.getName() == 'lookup' }
                basicService = lookupMethod.invoke(null,new Object[]{'javax.jnlp.BasicService'})
                if (basicService.showDocument(this.helpURL)) {
                        return
                }
        }
        // fallback
        pane = swing.optionPane(message:"See ${this.helpURL}")
        dialog = pane.createDialog(frame,'Help')
        dialog.show()

    }

    showAbout(EventObject evt = null) {
        version = InvokerHelper.getVersion()
        pane = swing.optionPane(message:'Console for evaluating Groovy JEScripts\nVersion ' + version + "\n Astrogrid Toolbox Version: " + (new Toolbox()).version)
        dialog = pane.createDialog(frame, 'About JEScript Console')
        dialog.show()
    }

    exit(EventObject evt = null) {
        if (scriptFile != null && dirty) {
            switch (JOptionPane.showConfirmDialog(frame,
                "Save changes to " + scriptFile.name + "?",
                "GroovyConsole", JOptionPane.YES_NO_CANCEL_OPTION))
            {
                case JOptionPane.YES_OPTION:
                    if (!fileSave())
                        break
                case JOptionPane.NO_OPTION:
                    frame.hide()
                    frame.dispose()
            }
        } else {
            frame.hide()
            frame.dispose()
        }
    }

}
