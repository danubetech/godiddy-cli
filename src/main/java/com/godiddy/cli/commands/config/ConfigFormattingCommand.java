package com.godiddy.cli.commands.config;

import com.godiddy.cli.api.Formatting;
import com.godiddy.cli.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "formatting",
        description = "Get or set the display of the Godiddy API requests and responses. Default value: " + Formatting.DEFAULT_FORMATTING + ".",
        mixinStandardHelpOptions = true
)
public class ConfigFormattingCommand extends ConfigAbstractCommand implements Callable<Integer> {

    private static final Logger log = LogManager.getLogger(ConfigFormattingCommand.class);

    @CommandLine.Parameters(
            index = "0",
            description = "The display of the Godiddy API requests and responses. Valid values: ${COMPLETION-CANDIDATES}. Default value: " + Formatting.DEFAULT_FORMATTING + ".",
            arity = "0..1"
    )
    Formatting.Value formatting;

    @Override
    public Integer call() throws Exception {
        log.trace("Parameter 'formatting': " + this.formatting);
        if (Boolean.TRUE.equals(this.remove)) {
            Configuration.setFormatting(null);
            System.out.println("Formatting successfully removed.");
        } else {
            if (this.formatting == null) {
                Formatting.Value formatting = Configuration.getFormatting();
                if (formatting == null) {
                    System.out.println("No formatting set.");
                } else {
                    System.out.println("Formatting: " + formatting);
                }
            } else {
                Formatting.Value formatting = this.formatting;
                Formatting.Value predefinedFormatting = Formatting.PREDEFINED_FORMATTINGS.get(formatting.name());
                if (predefinedFormatting != null) {
                    System.out.println("Using predefined formatting value '" + predefinedFormatting + "' for parameter value '" + formatting + "'.");
                    formatting = predefinedFormatting;
                }
                Configuration.setFormatting(formatting);
                System.out.println("Formatting successfully set.");
            }
        }
        return 0;
    }
}
