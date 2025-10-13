use std::str::FromStr as _;

use bigdecimal::BigDecimal;
use chumsky::prelude::*;
use num_bigint::BigInt;
use serde::{Serialize, Serializer};

#[derive(Debug, Clone, Serialize)]
#[serde(untagged, rename_all = "lowercase")]
pub enum FLiteral {
    #[serde(serialize_with = "self::serialize_bigint")]
    Integer(BigInt),
    #[serde(with = "bigdecimal::serde::json_num")]
    Real(BigDecimal),
    Boolean(bool),
    Null,
}

impl FLiteral {
    #[must_use]
    pub fn chumsky_parser<'src>()
    -> impl Parser<'src, &'src str, Self, extra::Err<Rich<'src, char>>> + Clone {
        let sign = choice((just('-'), just('+')));
        let digits = text::digits(10);

        let f_integer = sign.or_not().then(digits).to_slice().map(|it| {
            Self::Integer(BigInt::from_str(it).expect("bigint pattern should be correct"))
        });

        let f_real = sign
            .or_not()
            .then(digits)
            .then(just('.'))
            .then(digits)
            .to_slice()
            .map(|it| {
                Self::Real(BigDecimal::from_str(it).expect("bigdecimal pattern should be correct"))
            });

        let f_boolean = choice((
            text::keyword("false").to(Self::Boolean(false)),
            text::keyword("true").to(Self::Boolean(true)),
        ));

        let f_null = text::keyword("null").to(Self::Null);

        choice((f_real, f_integer, f_boolean, f_null))
            .then_ignore(crate::token_end::token_end().rewind())
    }
}

fn serialize_bigint<S: Serializer>(value: &BigInt, serializer: S) -> Result<S::Ok, S::Error> {
    serde_json::Number::from_str(&value.to_string())
        .map_err(serde::ser::Error::custom)?
        .serialize(serializer)
}
