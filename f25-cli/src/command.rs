use crate::cli::Command;

mod completions;
mod run;

/// CLI subcommand brancher.
pub fn execute(command: crate::cli::Command) -> miette::Result<()> {
    match command {
        Command::Completions(args) => self::completions::execute(&args),
        Command::Run(args) => self::run::execute(args)?,
    }
    Ok(())
}
