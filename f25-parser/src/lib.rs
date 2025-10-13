use chumsky::Parser as _;
use miette::{Diagnostic, NamedSource, SourceSpan};
use thiserror::Error;

pub use self::atom::FAtom;
pub use self::element::FElement;
pub use self::keyword::FKeyword;
pub use self::list::FList;
pub use self::literal::FLiteral;
pub use self::program::FProgram;

mod atom;
mod element;
mod keyword;
mod list;
mod literal;
mod program;
mod token_end;

pub fn parse_code_from(file_name: &str, source_code: String) -> miette::Result<FProgram> {
    if source_code.chars().all(char::is_whitespace) {
        return Ok(FProgram(Vec::new()));
    }

    let parse_result = FProgram::chumsky_parser().parse(&source_code).into_result();
    match parse_result {
        Ok(ast) => Ok(ast),
        Err(errors) => {
            let error = errors
                .first()
                .expect("if parsing failed, there should be at least one error");

            let len = {
                let span_len = error.span().into_range().len();
                if span_len > 1 { span_len } else { 0 }
            };
            let errors = (error.span().start, len).into();

            let error = self::ChumskyParseError {
                reason: error.to_string(),
                src: NamedSource::new(file_name, source_code).with_language("Lisp"),
                errors,
            };

            Err(error.into())
        }
    }
}

#[derive(Error, Debug, Diagnostic)]
#[error("invalid syntax")]
struct ChumskyParseError {
    reason: String,
    #[source_code]
    src: NamedSource<String>,
    #[label("{reason}")]
    errors: SourceSpan,
}
