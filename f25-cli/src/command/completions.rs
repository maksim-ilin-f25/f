use clap::CommandFactory as _;

pub fn execute(args: &crate::cli::CompletionsArgs) {
    args.shell
        .generate(&mut crate::cli::Cli::command(), &mut std::io::stdout());
}
