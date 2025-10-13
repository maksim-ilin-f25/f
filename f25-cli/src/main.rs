use clap::Parser as _;
use miette::IntoDiagnostic as _;

use self::cli::Cli;

mod ast;
mod cli;
mod command;

fn main() -> miette::Result<()> {
    let cli = Cli::parse();
    if let Some(ref command) = cli.command {
        self::command::execute(command);
        return Ok(());
    }

    let ast = self::ast::parse(cli.input)?;

    let ast_json = colored_json::to_colored_json_auto(&ast).into_diagnostic()?;
    println!("{ast_json}");

    Ok(())
}
