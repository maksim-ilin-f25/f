use crate::cli::Command;

mod completions;

/// CLI subcommand brancher.
pub fn execute(command: &crate::cli::Command) {
    match command {
        Command::Completions(args) => self::completions::execute(args),
    }
}
