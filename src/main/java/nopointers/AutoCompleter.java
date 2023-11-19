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

                        new StringsCompleter("\033[31mnew"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mCustom"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mRules"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mGuess"),

                        new NullCompleter()
                ),

                new ArgumentCompleter(
                        new StringsCompleter("\033[31mshow"),

                        new NullCompleter() 
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mshuffle"),

                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31msave"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mload"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mexit"),
                        new NullCompleter()
                ),
                new ArgumentCompleter(
                        new StringsCompleter("\033[31mhelp"),
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