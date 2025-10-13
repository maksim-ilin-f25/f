use chumsky::prelude::*;
use serde::Serialize;

use crate::{FAtom, FElement, FKeyword, FLiteral};

#[derive(Debug, Clone, Serialize)]
pub struct FList(pub(crate) Vec<FElement>);

impl FList {
    #[must_use]
    pub fn chumsky_parser<'src>()
    -> impl Parser<'src, &'src str, Self, extra::Err<Rich<'src, char>>> + Clone {
        recursive(|f_list| {
            let f_element = recursive(|f_element| {
                choice((
                    FAtom::chumsky_parser().map(FElement::Atom),
                    FLiteral::chumsky_parser().map(FElement::Literal),
                    FKeyword::chumsky_parser().map(FElement::Keyword),
                    f_list.map(FElement::List),
                    just('\'')
                        .ignore_then(f_element)
                        .map(|it| FElement::Quote(Box::new(it))),
                ))
            });

            f_element
                .padded()
                .repeated()
                .collect()
                .delimited_by(just('('), just(')'))
                .map(Self)
        })
    }
}
