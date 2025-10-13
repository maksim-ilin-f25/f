use std::io::Read as _;

use f25_parser::FProgram;
use miette::{IntoDiagnostic as _, miette};

pub fn execute(args: crate::cli::RunArgs) -> miette::Result<()> {
    let ast = self::parse(args.input)?;

    let ast_json = colored_json::to_colored_json_auto(&ast).into_diagnostic()?;
    println!("{ast_json}");

    Ok(())
}

fn parse(mut input: clio::Input) -> miette::Result<FProgram> {
    let mut source_code = String::new();
    input.read_to_string(&mut source_code).into_diagnostic()?;

    let file_name = if input.is_std() {
        "stdin"
    } else {
        input
            .path()
            .to_str()
            .ok_or_else(|| miette!("file path is invalid utf-8"))?
    };

    f25_parser::parse_code_from(file_name, source_code)
}
