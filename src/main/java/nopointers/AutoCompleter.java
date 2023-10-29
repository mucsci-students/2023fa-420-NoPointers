package nopointers;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.ParsedLine;
import org.jline.reader.LineReader;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleter implements Completer {
    private final StringsCompleter delegate;

    public AutoCompleter() {
        delegate = new StringsCompleter();
    }


    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        delegate.complete(reader,line,candidates);
    }
}