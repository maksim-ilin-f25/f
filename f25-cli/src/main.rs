use clap::Parser as _;

use self::cli::Cli;

mod cli;
mod command;

fn main() -> miette::Result<()> {
    let cli = Cli::parse();
    self::command::execute(cli.command)
}
