use chumsky::prelude::*;
use serde::Serialize;

#[derive(Debug, Clone, Serialize)]
#[serde(rename_all = "lowercase")]
pub enum FKeyword {
    Quote,
    Setq,
    Func,
    Lambda,
    Prog,
    Cond,
    While,
    Return,
    Break,
}

impl FKeyword {
    #[must_use]
    pub fn chumsky_parser<'src>()
    -> impl Parser<'src, &'src str, Self, extra::Err<Rich<'src, char>>> + Clone {
        choice((
            text::keyword("quote").to(Self::Quote),
            text::keyword("setq").to(Self::Setq),
            text::keyword("func").to(Self::Func),
            text::keyword("lambda").to(Self::Lambda),
            text::keyword("prog").to(Self::Prog),
            text::keyword("cond").to(Self::Cond),
            text::keyword("while").to(Self::While),
            text::keyword("return").to(Self::Return),
            text::keyword("break").to(Self::Break),
        ))
        .then_ignore(crate::token_end::token_end().rewind())
    }
}
