use chumsky::prelude::*;
use serde::Serialize;

use crate::{FAtom, FElement, FKeyword, FList, FLiteral};

#[derive(Debug, Clone, Serialize)]
pub struct FProgram(Vec<FElement>);

impl FProgram {
    #[must_use]
    pub fn chumsky_parser<'src>() -> impl Parser<'src, &'src str, Self, extra::Err<Rich<'src, char>>>
    {
        let f_element = recursive(|f_element| {
            let f_list = f_element
                .clone()
                .padded()
                .repeated()
                .collect()
                .delimited_by(just('('), just(')'))
                .map(FList);

            choice((
                FLiteral::chumsky_parser().map(FElement::Literal),
                FKeyword::chumsky_parser().map(FElement::Keyword),
                FAtom::chumsky_parser().map(FElement::Atom),
                f_list.map(FElement::List),
                just('\'')
                    .ignore_then(f_element)
                    .map(|it| FElement::Quote(Box::new(it))),
            ))
        });

        f_element.padded().repeated().collect().map(Self)
    }
}
