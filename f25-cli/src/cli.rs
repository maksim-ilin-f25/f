use clap::{Args, Parser, Subcommand, crate_description};

#[derive(Debug, Parser)]
#[command(
    arg_required_else_help = true,
    styles = clap_cargo::style::CLAP_STYLING,
    about = crate_description!(),
    version,
)]
pub struct Cli {
    /// Path to the file to run, use '-' for stdin
    #[clap(value_parser)]
    pub input: clio::Input,

    #[command(subcommand)]
    pub command: Option<Command>,
}

#[derive(Debug, Subcommand)]
pub enum Command {
    /// Generate shell completion scripts for this CLI
    Completions(CompletionsArgs),
}

#[derive(Debug, Args)]
pub struct CompletionsArgs {
    pub shell: clap_complete_command::Shell,
}
