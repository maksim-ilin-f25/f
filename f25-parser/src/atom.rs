use chumsky::prelude::*;
use compact_str::CompactString;
use serde::Serialize;

use crate::{FKeyword, FLiteral};

#[derive(Debug, Clone, Serialize)]
pub struct FAtom(CompactString);

impl FAtom {
    #[must_use]
    pub fn chumsky_parser<'src>()
    -> impl Parser<'src, &'src str, Self, extra::Err<Rich<'src, char>>> + Clone {
        text::ident()
            .and_is(FKeyword::chumsky_parser().not())
            .and_is(FLiteral::chumsky_parser().not())
            .map(|it| Self(CompactString::new(it)))
            .then_ignore(crate::token_end::token_end().rewind())
    }
}
