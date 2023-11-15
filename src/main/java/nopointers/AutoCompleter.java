package nopointers;

import java.util.ArrayList;
import java.util.Collection;

import org.jline.reader.Completer;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

/**
 * Tab Completer class for the UML Diagram. Establishes words meant for completion.
 */

public class AutoCompleter {

    private AggregateCompleter completer;

    public AutoCompleter(){
        this.completer = new AggregateCompleter(
                new ArgumentCompleter(
                        new StringsCompleter("new"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("Custom"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("Rules"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("Guess"),

                        new NullCompleter()
                ),

                new ArgumentCompleter(
                        new StringsCompleter("show"),

                        new NullCompleter() 
                ),
                new ArgumentCompleter(
                        new StringsCompleter("shuffle"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("save"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("load"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("exit"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("help"),
                        new NullCompleter()
                )
        );
    }

    public AggregateCompleter updateCompleter(){
        Collection<Completer> completers = completer.getCompleters();
        completers = new ArrayList<>(completers);
        return new AggregateCompleter(completers);
    }


}