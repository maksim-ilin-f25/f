use std::io::Read as _;

use f25_parser::FProgram;
use miette::{IntoDiagnostic as _, miette};

pub fn parse(mut input: clio::Input) -> miette::Result<FProgram> {
    let mut source_code = String::new();
    input.read_to_string(&mut source_code).into_diagnostic()?;

    let file_name = if input.is_std() {
        None
    } else {
        Some(
            input
                .path()
                .to_str()
                .ok_or_else(|| miette!("file path is invalid utf-8"))?,
        )
    };

    f25_parser::parse_code_from(file_name, source_code)
}
