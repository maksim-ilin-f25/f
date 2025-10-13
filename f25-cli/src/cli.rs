use clap::{Args, Parser, Subcommand, crate_description};

#[derive(Debug, Parser)]
#[command(
    arg_required_else_help = true,
    styles = clap_cargo::style::CLAP_STYLING,
    about = crate_description!(),
    version,
)]
pub struct Cli {
    #[command(subcommand)]
    pub command: Command,
}

#[derive(Debug, Subcommand)]
pub enum Command {
    /// Execute an F source code file
    Run(RunArgs),
    /// Generate shell completion scripts for this CLI
    Completions(CompletionsArgs),
}

#[derive(Debug, Args)]
pub struct RunArgs {
    /// Path to the file to run, use '-' for stdin
    #[clap(value_parser)]
    pub input: clio::Input,
}

#[derive(Debug, Args)]
pub struct CompletionsArgs {
    pub shell: clap_complete_command::Shell,
}
